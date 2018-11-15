import { IUser } from "app/core/user/user.model";

export interface IField {
  id?: number;
  section?: string;
  year?: number;
  belongs?: IUser;
}

export class Field implements IField {
  constructor(
    public id?: number,
    public section?: string,
    public year?: number,
    public belongs?: IUser
  ) {}
}
