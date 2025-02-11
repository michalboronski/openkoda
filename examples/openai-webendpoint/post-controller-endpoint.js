flow.thenSet("conversationId", a => {
	if(a.params.get("isFirstMessage") === 'true') {
		return a.services.openAI.sendMessageToGPT(
			a.params.get("userMessage"),
			"gpt-4-0613", // after a lot of experiments, this model works the best
			"0.2",
			'property', 'agent', 'client' // <- here set app entities that should be in prompt
		);
	} else {
		return a.services.openAI.sendMessageToGPT(a.params.get("userMessage"), a.params.get("conversationId"));
	}
});

//IMPORTANT:
//response type - MODEL_AS_JSON
//model attribute - response,conversationId