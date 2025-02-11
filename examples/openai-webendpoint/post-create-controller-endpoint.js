flow.then(a => {
    let frRepository = a.services.data.getRepository('frontendResource');
    let fr = a.params.get("frontendResourceId") == null ? null : frRepository.findOne(a.services.util.parseLong(a.params.get("frontendResourceId")));
    if (fr == null) {
        fr = frRepository.getNew();
        fr.setType("HTML");
        fr.setResourceType("UI_COMPONENT");
        fr.setAccessLevel(Java.type("com.openkoda.model.component.FrontendResource.AccessLevel").GLOBAL);
        fr.setName(a.params.get("id"));
    }
    fr.setContent(a.params.get("htmlCode"));
    fr = frRepository.saveOne(fr);
    return fr;
})
.thenSet("frontendResourceId", a => {
    let ceRepository = a.services.data.getRepository('controllerEndpoint');
    let ce = ceRepository.findOne( (root, query, cb) => {
        return cb.and(cb.equal(root.get("id"), a.result.getId()), cb.equal(root.get("httpMethod"), a.params.get("httpMethod")), cb.equal(root.get("subPath"), a.params.get("subPath")));
    });
    if (ce == null) {
        ce = ceRepository.getNew();
        ce.setHttpMethod(a.params.get("httpMethod"));
        ce.setSubPath(a.params.get("subPath"));
        ce.setFrontendResourceId(a.result.getId());
        ce.setModelAttributes("response");
    }
    ce.setCode(a.params.get("flowCode"));
    ce.setResponseType(a.params.get("flowResponseType"));
    ce = ceRepository.saveOne(ce);

    return a.result.getId();
});


//POST, MODEL_AS_JSON
//model attributes: frontendResourceId,controllerEndpointSubPath