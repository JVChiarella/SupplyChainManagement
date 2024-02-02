export async function PostEmployee(username : any, password : any){
    await fetch('http://localhost:8080/login/employee', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password  })
        }).then(async (response) => {
            if(response.ok){
                const result = await response.json();
                localStorage.setItem("user", JSON.stringify(result));
                console.log(result);
                return result;
            } else {
                //handle errors
                return null;
            }
        }
    )
}