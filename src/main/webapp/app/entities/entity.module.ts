
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { InsatplatformSlackChannelModule } from './slack-channel/slack-channel.module';
import { InsatplatformFieldModule } from "./field/field.module";
import { InsatplatformSlackUserModule } from "./slack-user/slack-user.module";
import { InsatplatformIntegrationModule } from "./integration/integration.module";


/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
  // prettier-ignore
  imports: [
        InsatplatformFieldModule,
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
