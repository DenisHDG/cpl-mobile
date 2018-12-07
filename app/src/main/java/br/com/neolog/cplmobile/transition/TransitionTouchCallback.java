package br.com.neolog.cplmobile.transition;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.android.gms.tasks.Task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import br.com.neolog.cplmobile.R;
import br.com.neolog.cplmobile.di.Injectable;
import br.com.neolog.cplmobile.exception.RemoteMessageHandler;
import br.com.neolog.cplmobile.location.LocationService;
import br.com.neolog.cplmobile.modal.ModalFactory;
import br.com.neolog.monitoring.monitorable.model.api.transition.TransitionStatus;
import br.com.neolog.monitoring.monitorable.model.rest.RestRemoteMessage;
import br.com.neolog.monitoring.monitorable.model.rest.transition.RestFinalizeTransitionDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TransitionTouchCallback
        extends
        ItemTouchHelper.Callback
        implements
        Injectable {

    private enum ButtonsState
    {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE,
        INVISIBLE
    }

    private boolean swipeBack = false;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private RectF buttonInstance = null;
    private static final float buttonWidth = 300;

    private final Context context;
    private final TransitionGroupAdapter transitionsAdapter;
    private final TransitionService transitionService;
    private final LocationService locationService;
    private RecyclerView.ViewHolder currentItemViewHolder;
    private final RemoteMessageHandler remoteMessageHandler;
    private final Runnable onSuccessCallback;




    public TransitionTouchCallback(
            @NonNull final Context context,
            @NonNull final TransitionGroupAdapter transitionsAdapter,
            @NonNull final TransitionService transitionService,
            @Nonnull final RemoteMessageHandler remoteMessageHandler,
            @Nonnull final LocationService locationService,
            @Nonnull final Runnable onSuccessCallback)

    {
        this.context = context;
        this.transitionsAdapter = transitionsAdapter;
        this.transitionService = transitionService;
        this.remoteMessageHandler = remoteMessageHandler;
        this.locationService = locationService;
        this.onSuccessCallback = onSuccessCallback;
    }

    @Override
    public int getMovementFlags(
            @NonNull final RecyclerView recyclerView,
            @NonNull final RecyclerView.ViewHolder viewHolder)
    {
        final int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        final int dragFlags = 0; // sem movimento de drag
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(
            @NonNull final RecyclerView recyclerView,
            @NonNull final RecyclerView.ViewHolder viewHolder,
            @NonNull final RecyclerView.ViewHolder viewHolder1)
    {
        return false;
    }

         @Override
        public void onSwiped(
                @Nonnull final RecyclerView.ViewHolder viewHolder,
                final int direction)
         {
         }

    private void finalize(
        @NonNull final TransitionGroupAdapterItem transitionGroupAdapterItem,
        @NonNull final TransitionStatus transitionStatus)
    {
        final Task<Location> lastLocation = locationService.getLastLocation( context );
        if( lastLocation == null ) {
            remoteMessageHandler.showModal( R.string.error_on_obtain_lat_long, context );
            return;
        }
        lastLocation.addOnSuccessListener( location -> {
            List<RestFinalizeTransitionDTO> transitions;
            final boolean isStop = transitionGroupAdapterItem.getGroup() != null;
            if( isStop ) {
                transitions = TransitionGroupConverter.convert( transitionStatus, transitionGroupAdapterItem.getGroup(), location );
            } else {
                transitions = TransitionGroupConverter.convert( transitionStatus, transitionGroupAdapterItem.getTransition(), location );
            }
            if( transitions.isEmpty() ) {
                remoteMessageHandler.showModal( isStop ? R.string.transactions_in_stop_already_finalized
                    : R.string.transaction_already_finalized, context );
                return;
            }

            if( isStop ) {
                confirmAction( R.string.trip_stop_group_message, transitions, status( transitionStatus ) );
            } else {
                confirmAction( R.string.trip_stop_transition_message, transitions, status( transitionStatus ) );
            }
        } ).addOnFailureListener( t -> remoteMessageHandler.showModal( t, context ) );
    }

    private void doFinalize(
        @NonNull final List<RestFinalizeTransitionDTO> transitions )
    {
        transitionService.finalize( transitions ).enqueue( new Callback<List<RestRemoteMessage>>() {
            @Override
            public void onResponse(
                @NonNull final Call<List<RestRemoteMessage>> call,
                @NonNull final Response<List<RestRemoteMessage>> response )
            {
                if( ! response.isSuccessful() ) {
                    remoteMessageHandler.showModal( response, context );
                } else if( response.body() == null ) {
                    remoteMessageHandler.showModal( R.string.remote_exception_handler_internal_server_error, context );
                } else {
                    remoteMessageHandler.showModal( response.body(), context );
                }
                onSuccessCallback.run();
            }

            @Override
            public void onFailure(
                @NonNull final Call<List<RestRemoteMessage>> call,
                @NonNull final Throwable t )
            {
                remoteMessageHandler.showModal( t, context );
            }
        } );
    }

    /**
     * method that add new confirmation screen for status change
     *
     * @param resStringId
     * @param transitions {@link List} of single transition or transitions group
     */
    private void confirmAction(@StringRes final int resStringId,
        @NonNull final List<RestFinalizeTransitionDTO> transitions,
        @NonNull final String status )
    {
        ModalFactory.with( context )
            .confirm( context.getString( resStringId, status ), () -> doFinalize( transitions ) )
            .show();
    }

    /**
     * method to get status name
     *
     * @param transitionStatus {@link TransitionStatus} with all possible
     *        transitions
     * @return {@link String} containing status name
     */
    private String status(
        final TransitionStatus transitionStatus )
    {
        final String status;

        switch( transitionStatus ) {
            case FINALIZED:
                status = "realizado";
                break;
            case IN_EXECUTION:
                status = "não finalizado";
                break;
            case NOT_ACCOMPLISHED:
                status = "não realizado";
                break;
            case CANCELLED:
                status = "cancelado";
                break;
            default:
                throw new IllegalArgumentException();
        }

        return status;
    }

    // ITENS ADICIONADOS PARA REALIZAR O SWIPE MOSTRANDO 2 OPÇÕES
    @Override
    public int convertToAbsoluteDirection(
            final int flags,
            final int layoutDirection)
    {
        if (swipeBack)
        {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(
            @Nonnull final Canvas c,
            @Nonnull final RecyclerView recyclerView,
            @Nonnull final RecyclerView.ViewHolder viewHolder,
            float dX,
            final float dY,
            final int actionState,
            final boolean isCurrentlyActive)
    {

        if (actionState == ACTION_STATE_SWIPE)
        {
            if (buttonShowedState != ButtonsState.GONE)
            {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else
                {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
        drawButtons(c, currentItemViewHolder);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(
            final Canvas c,
            final RecyclerView recyclerView,
            final RecyclerView.ViewHolder viewHolder,
            final float dX,
            final float dY,
            final int actionState,
            final boolean isCurrentlyActive)
    {
        recyclerView.setOnTouchListener((v, event) -> {
            swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
            if (swipeBack)
            {
                if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                else if (dX > buttonWidth) buttonShowedState = ButtonsState.LEFT_VISIBLE;

                if (buttonShowedState != ButtonsState.GONE)
                {
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    setItemsClickable(recyclerView, false);
                }
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(
            final Canvas c,
            final RecyclerView recyclerView,
            final RecyclerView.ViewHolder viewHolder,
            final float dX,
            final float dY,
            final int actionState,
            final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(
            final Canvas c,
            final RecyclerView recyclerView,
            final RecyclerView.ViewHolder viewHolder,
            final float dX,
            final float dY,
            final int actionState,
            final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                TransitionTouchCallback.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener((v1, event1) -> false);
                setItemsClickable(recyclerView, true);
                swipeBack = false;

                if (buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                    if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {

                        final TransitionGroupAdapterItem transitionsAdapterItem = transitionsAdapter.getItem( viewHolder.getAdapterPosition());
                        assert transitionsAdapterItem != null;
                        finalize( transitionsAdapterItem, TransitionStatus.FINALIZED );

                    } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {

                        final TransitionGroupAdapterItem transitionsAdapterItem = transitionsAdapter.getItem( viewHolder.getAdapterPosition());
                        assert transitionsAdapterItem != null;
                        finalize( transitionsAdapterItem, TransitionStatus.NOT_ACCOMPLISHED );
                    }
                }

                buttonShowedState = ButtonsState.GONE;
                currentItemViewHolder = null;
            }
            return false;
        });
    }


    private void setItemsClickable(final RecyclerView recyclerView, final  boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private void drawButtons(final Canvas c, final RecyclerView.ViewHolder viewHolder) {
        final float buttonWidthWithoutPadding = buttonWidth - 20;
        final float corners = 20;
        final Bitmap icon_realizar;
        final Bitmap icon_nao_realizar;
        final View itemView = viewHolder.itemView;
        final Paint p = new Paint();
        p.setColor(Color.parseColor("#FFFFFF"));
        icon_realizar = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_realizar);
        icon_nao_realizar = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_nao_realizar);

        //Button Right

        final RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        c.drawRoundRect(leftButton, corners, corners, p);
        c.drawBitmap(icon_realizar, (float) itemView.getLeft() + (((buttonWidthWithoutPadding / 2) - (icon_realizar.getHeight() / 2 ))),
                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon_realizar.getHeight()) / 2, p);
        drawText("REALIZAR", c, leftButton, p);

        p.setColor(Color.parseColor("#FFFFFF"));
        final RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        //Button Left
        c.drawRoundRect(rightButton, corners, corners, p);
        c.drawBitmap(icon_nao_realizar, (float) itemView.getRight() - (((buttonWidthWithoutPadding / 2) + (icon_nao_realizar.getHeight() / 2 ))),
                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon_nao_realizar.getHeight())/2,
                p);
        drawText("Não \n Realizar", c, rightButton, p);

        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightButton;
        }
    }
    private void drawText(
            final String text,
            final Canvas c,
            final RectF button,
            final Paint p)
    {
        final float textSize = 40;
        p.setColor(Color.parseColor("#25B99A"));
        p.setFakeBoldText(true);
        p.setAntiAlias(false);
        p.setTextSize(textSize);
        final float textWidth = p.measureText(text);
        c.drawText(text,
                button.centerX()-(textWidth/2),
                (button.centerY()+(textWidth /2)),
                p);
    }
}
