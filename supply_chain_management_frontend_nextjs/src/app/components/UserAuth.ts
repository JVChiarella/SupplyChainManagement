export async function userAuth(){
    const { user, error } = await getUser();
    return {user, error}
};

async function getUser() {
    const res = await fetch('/api/auth', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
    });
    
    //if auth api returns anything but success, throw error and redirect to login
    if(res.statusText != "OK"){
        return {
            user: null,
            error: "Unauthorized"
        }
    }

    const data = await res.json();
    return {
        user: data,
        error: null,
    };
}