export interface IChannel {
  id?: number;
  channelID?: string;
  name?: string;
  teamID?: string;
  isPrivate?: boolean;
  purpose?: string;
}

export class Channel implements IChannel {
  constructor(
    public id?: number,
    public channelID?: string,
    public name?: string,
    public teamID?: string,
    public isPrivate?: boolean,
    public purpose?: string
  ) {
    this.isPrivate = this.isPrivate || false;
  }
}
