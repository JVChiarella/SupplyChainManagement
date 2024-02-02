type Order = {
    id? : number;
    customer : Customer;
    invoice : Invoice;
    orderedItems : OrderedItem;
    timestamp : Date;
}

type UserCredentials = {
    username: string;
    password: string;
}

type Invoice = {
    id? : number;
    order : Order;
    customerID : number;
    totalPrice : number;
}

type OrderedItem = {
    id? : number;
    stockID : number;
    amount : number;
}

type StockItem = {
    id? : number;
    count : number;
    price : number;
}

type Customer = {
    id?: number;
    credentials: UserCredentials;
    firstName: string;
    lastName: string;
    email: string;
    address : string;
    phoneNumber: string;
    active: boolean;
    orders?: Order[];
};

type Employee = {
    id?: number;
    credentials: UserCredentials;
    firstName: string;
    lastName: string;
    email: string;
    admin: boolean;
    active: boolean;
    orders?: Order[];
};

type User = {
    id?: number;
    credentials: UserCredentials;
    firstName: string;
    lastName: string;
    email: string;
    active: boolean;
    address? : string;
    phoneNumber?: string;
    admin?: boolean;
    orders?: Order[];
};