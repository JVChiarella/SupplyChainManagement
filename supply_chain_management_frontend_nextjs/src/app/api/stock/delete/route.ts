import { cookies } from "next/headers";
import { COOKIE_NAME } from "@/app/constants";

export async function PATCH(request : Request){
    //convert body of request
    const data = await request.json();

    //get token from cookies
    const cookieStore = cookies();
    const token = cookieStore.get(COOKIE_NAME);
    const auth = "Bearer " + token?.value;

    try{
        //verify jwt in spring
        const response = await fetch(`http://localhost:8080/verification`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' ,
                        'Authorization': auth},
            }).then(async (res) => {
                const result = await res.json();
                if(result.message == "verified successfully"){
                    //soft delete stock in db via spring api
                    const id = data.id;
                    const post = await fetch(`http://localhost:8080/stock/delete/${id}`, {
                        method: 'PATCH',
                        headers: { 'Content-Type': 'application/json' ,
                                'Authorization': auth}
                    });

                    const newStock = await post.json();
                    return new Response(JSON.stringify(newStock), {
                        status: 200,
                    })}
            }, () => {
                return new Response(JSON.stringify("JWT validation failed"), {
                    status: 401,
                })
            })

        return response;
    } catch(error){
        return new Response(JSON.stringify(error), {
            status: 401,
        })
    }
}