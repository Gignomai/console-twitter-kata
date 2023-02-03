package com.gignomai.consoleTwitter.domain.port.primary;

import java.io.InputStream;

public interface CommandListenerService {

    void listen(InputStream inputStream);
}
