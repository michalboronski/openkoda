/*
MIT License

Copyright (c) 2016-2024, Openkoda CDX Sp. z o.o. Sp. K. <openkoda.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice
shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.openkoda.uicomponent.live;

import com.openkoda.repository.SearchableRepositories;
import com.openkoda.service.openai.ChatGPTPromptService;
import com.openkoda.service.openai.ChatGPTService;
import com.openkoda.uicomponent.OpenAIServices;
import jakarta.inject.Inject;
import org.springframework.stereotype.Component;


@Component
public class LiveOpenAIServices implements OpenAIServices {
    @Inject
    ChatGPTService chatGPTService;
    @Inject
    ChatGPTPromptService promptService;
    @Override
    public String sendMessageToGPT(String message, String model, String temperature, String... repositoryNames) {
        return chatGPTService.sendMessageToGPT(null, message, model, temperature, null, repositoryNames);
    }

    @Override
    public String sendMessageToGPT(String message, String conversationId) {
        return chatGPTService.sendMessageToGPT(message, conversationId, null);
    }

    @Override
    public String sendMessageToGPTWithPrompt(String promptFileName, String message, String model, String temperature,
                                             String... repositoryNames) {
        return chatGPTService.sendMessageToGPT(promptFileName, message, model, temperature, null, repositoryNames);
    }

    @Override
    public String getCompleteDataSchemaPrompt() {
        return promptService.getDataSchemas(SearchableRepositories.getDynamicSearchableRepositoriesEntityKeys());
    }
}