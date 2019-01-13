import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { InsatIntegrationModule } from './integration/integration.module';
import { InsatFieldModule } from './field/field.module';
import { InsatSlackUserModule } from './slack-user/slack-user.module';
import { InsatSlackChannelModule } from './slack-channel/slack-channel.module';
import { InsatClubModule } from './club/club.module';

import { InsatJoinClubRequestModule } from './join-club-request/join-club-request.module';
import { InsatInsatEventModule } from './insat-event/insat-event.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        InsatIntegrationModule,
        InsatFieldModule,
        InsatSlackUserModule,
        InsatSlackChannelModule,
        InsatClubModule,
        InsatJoinClubRequestModule,
        InsatInsatEventModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatEntityModule {}
