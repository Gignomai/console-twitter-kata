package com.gignomai.consoleTwitter;

import com.gignomai.consoleTwitter.domain.service.CommandProcessingService;
import com.gignomai.consoleTwitter.infrastructure.console.adapter.ConsoleCommandListenerServiceAdapter;

public class ConsoleTwitterApplication {

    public static void main(String[] args) {
        new ConsoleCommandListenerServiceAdapter(new CommandProcessingService()).listen(System.in);
    }
}
