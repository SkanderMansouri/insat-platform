export interface IClub {
    id?: number;
    name?: string;
    president?: string;
    field?: string;
}

export class Club implements IClub {
    constructor(public id?: number, public name?: string, public president?: string, public field?: string) {}
}
