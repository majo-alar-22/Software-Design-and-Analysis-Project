# Soccer Manager
A sports league registration and management system, supporting accounts, players, captains, and administrators.

# Features

- Account management
- Sign up for accounts
- Create and join teams
- Kick players from your team
- Schedule matches with other teams

# Installation

## Option A: Maven
> [!IMPORTANT]  
> Make sure Maven is installed and available on your path. This should be available from within your package manager.

#### Step 1

`mvn package`

#### Step 2

Copy `target/ProjectTest-1.0-SNAPSHOT.jar` to a directory of your choosing

#### Step 3

Run `java -jar ProjectTest-1.0-SNAPSHOT.jar` in that directory.

# Setup Instructions for Users
* 1: When opening the app you will arrive at the login screen. If you already have an account, fill in your credentials in the fields provided and press 'Login' to continue. If you do not have an account, begin at the second step of instructions.

<p align="center">
  <img src = "images/Login-Screen.png" alt = "Login Screen Image" width = "850" height = "850">
</p>

* 2: (If you do not have an account): Press the 'Register' button at the bottom of the page on the Login page. You will be taken to the registration page where you must enter your username, password, and first and last name. press register to create your account. 
  * After registering, return to the login screen and enter your Username and Password. Press 'Login' to continue.
  
<p align = "center">
  <img src = "images/Registration-Page.png" alt = "Account registration image" width = "850" height = "850">
</p>

* 3: After logging in you will arrive at the main menu. You can navigate through each section of the app using the tabs located at the top right of the page. Navigate to the 'My Team" tab. Here you can create your team or request to be added to an existing team. Fill in the field and press 'Create a Team' to create your team.

<p align="center">
  <img src = "images/MyTeam-Page.png" alt = "My Team Creation page" width = "850" height = "850">
</p>

# Instructions on how to test/run program:
> [!IMPORTANT]  
> Make sure Maven and Java is installed and available on your path. This should be available in your distributions package manager or winget.

## Unix:
### 1st Step:
```bash
./Unix/build.sh
```

#### If error "Permission denied":
```
chmod +x ./Unix/*.sh
```
### 2nd Step:
```
./Unix/run.sh
```
or 
```
./Unix/test.sh
```
## Windows:
### 1st Step:
```bat
build.bat
```
### 2nd Step:
```
run.bat
```
or
```
test.bat
```

