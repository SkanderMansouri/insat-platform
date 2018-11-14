import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";

import { InsatplatformSlackUserModule } from "./slack-user/slack-user.module";
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
  // prettier-ignore
  imports: [
        InsatplatformSlackUserModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatplatformEntityModule {}
