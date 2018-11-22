export interface IIntegration {
    id?: number;
    accessToken?: string;
    teamId?: string;
    scope?: string;
    score?: string;
    teamName?: string;
    teamUrl?: string;
    userId?:string;
}

export class Integration implements IIntegration {
    constructor(
        public id?: number,
        public accessToken?: string,
        public teamId?: string,
        public scope?: string,
        public score?: string,
        public teamName?: string,
        public teamUrl?: string,
        public userId?:string
    ) {}
}
