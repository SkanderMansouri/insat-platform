import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";

import { InsatplatformSlackUserModule } from "./slack-user/slack-user.module";
import { InsatplatformIntegrationModule } from "./integration/integration.module";
import { InsatplatformSlackChannelModule } from "./slack-channel/slack-channel.module";
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
  // prettier-ignore
  imports: [
        InsatplatformSlackUserModule,
        InsatplatformIntegrationModule,
        InsatplatformSlackChannelModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatplatformEntityModule {}
