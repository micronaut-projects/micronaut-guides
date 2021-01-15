To create a new guide create a new asciidoc file under `src/docs/guides` with the following template: 

```asciidoc
= @guideTitle@

@guideIntro@

Authors: @authors@

Micronaut Version: @micronaut@

include::{commondir}/common-gettingStarted.adoc[]

include::{commondir}/common-requirements.adoc[]

include::{commondir}/common-completesolution.adoc[]

include::{commondir}/common-create-app.adoc[]

TODO: Describe the user step by step how to write the app. Use includes to reference real code: 

Example of a Controller 

[source,@language@]
.src/main/@language@/example/micronaut/HelloController.@languageextension@
----
include::{sourceDir}/@sourceDir@/src/main/@language@/example/micronaut/HelloController.@languageextension@[]
----

Example of a Test

[source,@language@]
.src/test/@language@/example/micronaut/HelloControllerTest.@languageextension@
----
include::{sourceDir}/@sourceDir@/src/test/@language@/example/micronaut/HelloController@testsuffix@.@languageextension@[]
----

include::{commondir}/common-testApp.adoc[]

include::{commondir}/common-runapp.adoc[]

include::{commondir}/common-graal-with-plugins.adoc[]

:exclude-for-languages:groovy

TODO describe how you consume the endpoints exposed by the native image with curl

:exclude-for-languages:

== Next steps

TODO: link to the documentation modules you used in the guide

include::{commondir}/common-helpWithMicronaut.adoc[]
```