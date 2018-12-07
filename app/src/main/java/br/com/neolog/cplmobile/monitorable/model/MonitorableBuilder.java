package br.com.neolog.cplmobile.monitorable.model;

import br.com.neolog.monitoring.monitorable.model.api.StandardMonitorableType;

public class MonitorableBuilder
{
    private final int id;
    private final String code;
    private final StandardMonitorableType type;
    private LateStatus lateStatus = LateStatus.IN_TIME;
    private Integer parentId;
    private boolean root;

    public MonitorableBuilder(
        final int id,
        final String code,
        final StandardMonitorableType type )
    {
        this.id = id;
        this.code = code;
        this.type = type;
    }

    public MonitorableBuilder setLateStatus(
        final LateStatus lateStatus )
    {
        this.lateStatus = lateStatus;
        return this;
    }

    public MonitorableBuilder setParentId(
        final Integer parentId )
    {
        this.parentId = parentId;
        return this;
    }

    public MonitorableBuilder isRoot(
        final boolean root )
    {
        this.root = root;
        return this;
    }

    public Monitorable build()
    {
        return new Monitorable( id, code, type, lateStatus, parentId, root );
    }
}
