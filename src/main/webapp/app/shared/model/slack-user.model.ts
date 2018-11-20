export interface ISlackUser {
    id?: number;
    teamId?: string;
    slackId?: string;
    userName?: string;
    email?: string;
    isBot?: boolean;
}

export class SlackUser implements ISlackUser {
    constructor(
        public id?: number,
        public teamId?: string,
        public slackId?: string,
        public userName?: string,
        public email?: string,
        public isBot?: boolean
    ) {
        this.isBot = this.isBot || false;
    }
}
