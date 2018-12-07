package br.com.neolog.cplmobile.device;

import javax.inject.Inject;

public class DeviceService
{
    @Inject
    DeviceService()
    {
    }

    public String getDeviceId()
    {
        return "123";
    }

    public String getProviderId()
    {
        return "mobile";
    }
}
