import { NgModule } from '@angular/core';

import { InsatplatformSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [InsatplatformSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [InsatplatformSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class InsatplatformSharedCommonModule {}
