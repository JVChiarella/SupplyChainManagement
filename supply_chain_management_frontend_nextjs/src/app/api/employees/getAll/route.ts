import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";

export async function GET(){
    //get token from cookies
    const cookieStore = cookies();
    const token = cookieStore.get(COOKIE_NAME);
    const auth = "Bearer " + token?.value;

    //verify jwt in spring
    const response = await fetch(`http://localhost:8080/verification`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' ,
                    'Authorization': auth},
        }).then(async (res) => {
            const result = await res.json();
            if(result.message == "verified successfully"){
                //fetch requested data from spring api after token verification
                const result2 = await fetch(`http://localhost:8080/employees`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' ,
                            'Authorization': auth},
                });

                const users = await result2.json();
                return new Response(JSON.stringify(users), {
                    status: 200,
                })
            }
        }, () => {
            return new Response(JSON.stringify("JWT validation failed"), {
                status: 401,
            })
        })

    return response;
}