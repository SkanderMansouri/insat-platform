import { Route } from '@angular/router';

import { SidebarComponent } from './sidebar.component';

export const SidebarRoute: Route = {
    path: '',
    component: SidebarComponent,
    outlet: 'sidebar'
};
