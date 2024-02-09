import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";

export async function GET(){
    //get token from cookies
    const cookieStore = cookies();
    const token = cookieStore.get(COOKIE_NAME);
    const auth = "Bearer " + token?.value;

    //fetch data from spring api
    const res = await fetch(`http://localhost:8080/customers`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' ,
                'Authorization': auth},
    });

    const users = await res.json();
    return new Response(JSON.stringify(users))
}
