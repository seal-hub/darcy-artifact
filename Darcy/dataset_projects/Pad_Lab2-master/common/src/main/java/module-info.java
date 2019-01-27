module common {
    requires jackson.databind;
    requires jackson.core;
    requires commons.lang3;
    exports md.utm.fcim.service;
    exports md.utm.fcim.service.impl;
    exports md.utm.fcim.dto;
    exports md.utm.fcim.connection.tcp;
    exports md.utm.fcim.connection.tcp.impl;
    exports md.utm.fcim.connection.udp;
    exports md.utm.fcim.constant;
}