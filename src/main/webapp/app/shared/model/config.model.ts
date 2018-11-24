export interface IConfig {
    slackClientId?: string;
}

export class Config implements IConfig {
    constructor(public slackClientId?: string) {}
}
