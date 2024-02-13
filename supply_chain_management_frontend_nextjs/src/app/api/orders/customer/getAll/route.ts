import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";
import jwt from "jsonwebtoken";

export async function GET(){
    //get token from cookies
    const cookieStore = cookies();
    const token = cookieStore.get(COOKIE_NAME);
    const auth = "Bearer " + token?.value;

    //verify jwt in spring (jsonwebtoken auth in node does not work?)
    if(token){
        const response = await fetch(`http://localhost:8080/verification`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' ,
                        'Authorization': auth},
            }).then(async (res) => {
                const result = await res.json();
                if(result.message == "verified successfully"){
                    //get customer id from token
                    const options : any = jwt.decode(token?.value)
                    const id = options.id;

                    //fetch requested data from spring api after token verification
                    const result2 = await fetch(`http://localhost:8080/orders/customer/${id}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' ,
                                'Authorization': auth},
                    });

                    const orders = await result2.json();
                    return new Response(JSON.stringify(orders), {
                        status: 200,
                    })
                }
            }, () => {
                return new Response(JSON.stringify("JWT validation failed"), {
                    status: 401,
                })
            })

        return response;
    } else {
        return new Response(JSON.stringify("JWT not found in cookies"), {
            status: 401,
        })
    }
}
