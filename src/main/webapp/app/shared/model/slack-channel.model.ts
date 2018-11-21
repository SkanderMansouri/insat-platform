export interface ISlackChannel {
    id?: number;
    channelId?: string;
    name?: string;
    teamId?: string;
    isPrivate?: boolean;
}

export class SlackChannel implements ISlackChannel {
    constructor(public id?: number, public channelId?: string, public name?: string, public teamId?: string, public isPrivate?: boolean) {
        this.isPrivate = this.isPrivate || false;
    }
}
