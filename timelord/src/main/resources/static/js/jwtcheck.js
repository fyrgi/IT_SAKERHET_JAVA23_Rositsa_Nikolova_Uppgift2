function checkAuth() {
    const jwt = localStorage.getItem('jwt');
    if (jwt) {
        // Make a request with the JWT included in the Authorization header
        fetch('/protected-route', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + jwt
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok: ' + response.statusText);
                }
                return response.json(); // Try to parse JSON only if response is ok
            })
            .then(data => {
                console.log('Protected data:', data);
                // Proceed to the intended page
                const intendedPage = sessionStorage.getItem('intendedPage') || '/defaultPage.html';
                window.location.href = intendedPage;
            })
            .catch(error => {
                console.error('Error:', error);
                // Optionally handle specific error messages or redirect to login
                window.location.href = '/login'; // Redirect if there's an error
            });
    } else {
        console.log('User is not logged in.');
        // Redirect to login page if not logged in
        window.location.href = '/';
    }
}