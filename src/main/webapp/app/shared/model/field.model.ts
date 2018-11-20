export interface IField {
    id?: number;
    year?: number;
    section?: string;
}

export class Field implements IField {
    constructor(public id?: number, public year?: number, public section?: string) {}
}
