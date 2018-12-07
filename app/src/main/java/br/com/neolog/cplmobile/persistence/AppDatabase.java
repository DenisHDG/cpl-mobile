package br.com.neolog.cplmobile.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import br.com.neolog.cplmobile.monitorable.model.Monitorable;
import br.com.neolog.cplmobile.monitorable.model.MonitorableDao;
import br.com.neolog.cplmobile.monitorable.model.MonitorableFinish;
import br.com.neolog.cplmobile.monitorable.model.MonitorableFinishDao;
import br.com.neolog.cplmobile.monitorable.model.MonitorableProperty;
import br.com.neolog.cplmobile.monitorable.model.MonitorablePropertyDao;
import br.com.neolog.cplmobile.occurrence.Occurrence;
import br.com.neolog.cplmobile.occurrence.OccurrenceDao;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategory;
import br.com.neolog.cplmobile.occurrence.category.OccurrenceCategoryDao;
import br.com.neolog.cplmobile.occurrence.cause.AllowedMonitorableType;
import br.com.neolog.cplmobile.occurrence.cause.AllowedMonitorableTypeDao;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCause;
import br.com.neolog.cplmobile.occurrence.cause.OccurrenceCauseDao;
import br.com.neolog.cplmobile.persistence.converter.DateTimeConverter;
import br.com.neolog.cplmobile.persistence.converter.LateStatusConverter;
import br.com.neolog.cplmobile.persistence.converter.MonitorablePropertyTypeConverter;
import br.com.neolog.cplmobile.persistence.converter.StandardMonitorableTypeConverter;
import br.com.neolog.cplmobile.signal.Signal;
import br.com.neolog.cplmobile.signal.SignalDao;

@Database(
    entities = {
        AllowedMonitorableType.class,
        Monitorable.class,
        MonitorableFinish.class,
        MonitorableProperty.class,
        Occurrence.class,
        OccurrenceCategory.class,
        OccurrenceCause.class,
        Signal.class,
    }, version = AppDatabase.VERSION )
@TypeConverters( {
    DateTimeConverter.class,
    LateStatusConverter.class,
    MonitorablePropertyTypeConverter.class,
    StandardMonitorableTypeConverter.class
} )
public abstract class AppDatabase
    extends
        RoomDatabase
{
    static final String DATABASE_NAME = "cpl_mobile";
    static final int VERSION = 6;

    public abstract SignalDao getSignalDao();

    public abstract OccurrenceCategoryDao getOccurrenceCategoryDao();

    public abstract OccurrenceCauseDao getOccurrenceCauseDao();

    public abstract AllowedMonitorableTypeDao getAllowedMonitorableTypeDao();

    public abstract OccurrenceDao getOccurrenceDao();

    public abstract MonitorableDao getMonitorableDao();

    public abstract MonitorableFinishDao getMonitorableFinishDao();

    public abstract MonitorablePropertyDao getMonitorablePropertyDao();
}