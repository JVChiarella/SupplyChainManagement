type Order = {
    id? : number;
    customer_id : number;
    invoice : Invoice;
    ordered_items : OrderedItem[];
    timestamp : Date;
}

type UserCredentials = {
    username: string;
    password: string;
}

type Invoice = {
    id? : number;
    order? : Order;
    status: string;
    totalPrice : number;
}

type OrderedItem = {
    id? : number;
    stock_id : number;
    amount : number;
}

type StockItem = {
    id? : number;
    name: string,
    description: string,
    count : number;
    price : number;
}

type Customer = {
    id?: number;
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
    firstName: string;
    lastName: string;
    email: string;
    admin: boolean;
    active: boolean;
    invoices?: Invoice[];
};