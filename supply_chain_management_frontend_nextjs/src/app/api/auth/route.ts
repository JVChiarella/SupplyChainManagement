import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";

export async function GET(){
    const cookieStore = cookies();

    const token = cookieStore.get(COOKIE_NAME);
    if(token){
        //add token prefix
        const auth = "Bearer " + token?.value;
    
        //verify jwt in spring (jsonwebtoken auth in node does not work?)
        const response = await fetch(`http://localhost:8080/verification`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' ,
                    'Authorization': auth},
        }).then(async (res) => {
                const result = await res.json();
                if(result.message == "verified successfully"){
                    return new Response(JSON.stringify(result), {
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
        return new Response(JSON.stringify("JWT validation failed"), {
            status: 401,
        })
    }
}