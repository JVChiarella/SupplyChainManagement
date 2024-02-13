import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";

export async function POST(request : Request){
    //convert body of request
    const data = await request.json();

    //get token from cookies
    const cookieStore = cookies();
    const token = cookieStore.get(COOKIE_NAME);
    const auth = "Bearer " + token?.value;

    //verify jwt in spring
    if(token){
        const response = await fetch(`http://localhost:8080/verification`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' ,
                        'Authorization': auth},
            }).then(async (res) => {
                const result = await res.json();
                if(result.message == "verified successfully"){
                    //fetch requested data from spring api after token verification
                    const result2 = await fetch(`http://localhost:8080/orders/new`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' ,
                                'Authorization': auth},
                    body: JSON.stringify({ credentials: { username: data?.username, 
                                                          password: data?.password },
                                           firstName: data?.firstName,
                                           lastName: data?.lastName,
                                           active: true,
                                           admin: data?.admin,
                        })
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
