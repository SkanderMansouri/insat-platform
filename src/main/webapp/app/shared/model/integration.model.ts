import { ISlackUser } from "app/shared/model//slack-user.model";

export interface IIntegration {
  id?: number;
  accessToken?: string;
  scope?: string;
  teamId?: string;
  teamName?: string;
  botId?: string;
  botAccessToken?: string;
  userId?: number;
  teamUrl?: string;
  userIdSlackUserRelationships?: ISlackUser[];
}

export class Integration implements IIntegration {
  constructor(
    public id?: number,
    public accessToken?: string,
    public scope?: string,
    public teamId?: string,
    public teamName?: string,
    public botId?: string,
    public botAccessToken?: string,
    public userId?: number,
    public teamUrl?: string,
    public userIdSlackUserRelationships?: ISlackUser[]
  ) {}
}
