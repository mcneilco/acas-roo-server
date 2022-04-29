package com.labsynch.labseer.utils;

import org.springframework.security.web.firewall.DefaultHttpFirewall;

public class ACASHttpFirewall extends DefaultHttpFirewall {
    ACASHttpFirewall() {
        this.setAllowUrlEncodedSlash(true);
    }

}