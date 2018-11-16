export interface ISlackChannel {
  id?: number;
  channelId?: string;
  teamId?: string;
  name?: string;
  purpose?: string;
  isPrivate?: boolean;
}

export class SlackChannel implements ISlackChannel {
  constructor(
    public id?: number,
    public channelId?: string,
    public teamId?: string,
    public name?: string,
    public purpose?: string,
    public isPrivate?: boolean
  ) {
    this.isPrivate = this.isPrivate || false;
  }
}
