flow
    .then(a => a.params.get('id') != null ? a.services.data.getRepository("timelog").findOne( a.params.get('id')+"" ) : null)
    .thenSet("timelogEntity", a => a.services.data.saveForm(a.form, a.result))
    .thenSet("redirectUrl", a => "/html/organization/" + a.model.get("organizationEntityId") + "/cn/timelog" + (a.form.dto.get("startedOn") != null ? "?startedOn=" + a.form.dto.get("startedOn") : ""));