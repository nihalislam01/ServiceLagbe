# ServiceLagbe
User Guide:

Run the web application on your device by following the steps below.

Download Docker Desktop and write or copy paste these docker commands in your CLI
<pre><code>docker pull nihalislam/servicelagbe-servicelagbe</code></pre>
<pre><code>docker pull mysql:5.7</code></pre>
<pre><code>docker network create servicelagbe-net</code></pre>
<pre><code>docker run --detach --network servicelagbe-net --name servicelagbedb --env MYSQL_ROOT_PASSWORD=1234 --env MYSQL_USERNAME=servicelagbe --env MYSQL_PASSWORD=servicelagbe --env MYSQL_DATABASE=servicelagbedb --publish 3306:3306 mysql:5.7</code></pre>
<pre><code>docker run --network servicelagbe-net -p 8080:8080 --name servicelagbe nihalislam/servicelagbe-servicelagbe</code></pre>

After succesfully running, search this url in browser
http://localhost:8080/login

Introduction: 

Service Lagbe is a web based project. It is a service platform where both who are looking for work or in search for service can use this web application. 

Objectives:
1. Connect skilled household workers like electricians, plumbers, babysitters, caretakers ,cleaning people, drivers, car mechanics, health aides, housekeepers, maids, private nurses, and yard workers with the clients who are seeking for service.
2. Create a user friendly platform where everyone can serve clients.
3. A domestic skilled worker who does not know many people , but through our platform a worker can find many jobs in his region.

Users and their role:

There will be two sections for sign up. 

1. I am willing to work (Service provider)

Here people will create their profile using their phone number and 5 digit pin and pass in the information in which sector they are willing to work as well as their preferable regions.

Questions:
How do they get regular access to the internet?
They don't have to. They can go to a computer store and create their profile by passing the required information.
		 
2. I am in search of workers(Client)

Users can create their profile using their regular gmail id and password. Inside the application, they can search by filtering out region, time and even rating of the workers profile. They can also create different sections for their projects and add workers they hired from the application to keep track. 

Use cases of the system:

1. Creating Worker Profile: Workers have to use their phone number and a 5 digit pin number to sign up their account. Also they have to pass the information about their working location preferences and working posts. An OTP verification pin will be sent to their number to verify. Then they can login to their account. They can turn off their availability status whenever they want and they will be removed from search results.

2. Creating General Profile: In general, people can sign up their account using gmail and password. An Email verification link will be sent to their email to verify then they can login to their account. Otherwise they won't be able to login to their account.

3. Searching for service: Users can filter out their preferred region and search for workers who are available. By default the result will be sorted by recently created account users. If their desire matches, they can contact and negotiate with them by using the given contact info in the worker’s profile.

4. Searching for work: General users can post for their preferred work application and workers can apply by just one click. It will notify general users. However, not everyone will have regular access to the internet. They can also just create their working profile and set their availability status on so that general users can find them in the application.
   
5. Creating posts: General users can create different posts where they can add workers who are working for them to keep track. For example: If a general user wants to renovate their residence, they can create a section named Renovation and add the workers they recruited from the application and keep track. They can create as many sections they want. Once they are done with their project, they can delete their project section.

6. Rating system: The general users can rate them from their experience with that worker. Also how many general users rated them will be available on the worker's profile. 

7. Language: In this application the language will be by default Bangla for everyone’s better understanding. However, users can change them into English if they want.

8. Reset Password: If a user forgets their password can reset their password by verifying their email (general users) or phone number (worker users).

Conclusion:

A system which has  a user-friendly platform, transparent and easy to use for everyone aims to change the way household workers work. A client can book a worker for his/her household work without going out and finding someone. Also it does not guarantee that man is skilled or not.  A worker who does not know where someone needs him. So, this platform  can help a client to find workers(intermediate or skilled) from many people and a worker to find many jobs nearby.

Collaborated with @shahin-dev

