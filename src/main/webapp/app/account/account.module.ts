import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsatSharedModule } from 'app/shared';

import {
    PasswordStrengthBarComponent,
    RegisterComponent,
    ActivateComponent,
    PasswordComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    accountState
} from './';
import { InviteComponent } from './invite/invite.component';

@NgModule({
    imports: [InsatSharedModule, RouterModule.forChild(accountState)],
    declarations: [
        ActivateComponent,
        RegisterComponent,
        PasswordComponent,
        PasswordStrengthBarComponent,
        PasswordResetInitComponent,
        PasswordResetFinishComponent,
        SettingsComponent,
        InviteComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsatAccountModule {}
