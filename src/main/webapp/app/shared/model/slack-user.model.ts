export interface ISlackUser {
  id?: number;
  slackId?: number;
  realName?: string;
  email?: string;
  teamId?: number;
}

export class SlackUser implements ISlackUser {
  constructor(
    public id?: number,
    public slackId?: number,
    public realName?: string,
    public email?: string,
    public teamId?: number
  ) {}
}
