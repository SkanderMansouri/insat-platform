import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { InsatIntegrationModule } from './integration/integration.module';
import { InsatFieldModule } from './field/field.module';
import { InsatSlackUserModule } from './slack-user/slack-user.module';
import { InsatSlackChannelModule } from './slack-channel/slack-channel.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        InsatIntegrationModule,
        InsatFieldModule,
        InsatSlackUserModule,
        InsatSlackChannelModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatEntityModule {}
