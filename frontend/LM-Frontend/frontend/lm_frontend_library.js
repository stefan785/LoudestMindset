let serverAddress = "http://127.0.0.1:8080";

// 4 in 1 fetch function
// uses PUT , POST , GET , DELETE as methods
// returns response
async function fetchFn(link, method, apiKey, requestBody) {
  let generalResponse = "";

  try {
    await fetch(serverAddress + link, {
      method: method,
      headers: {
        "Content-Type": "application/json",
        apiKey: apiKey,
      },
      body: JSON.stringify(requestBody),
    })
      .then((response) => response.json())
      .then((data) => {
        generalResponse = Object.assign(data);
      })
      .catch((error) => console.error("Error:", error));
  } catch (Error) {
    console.log("cannot fetch: " + Error);
    return "error";
  }

  return generalResponse;
}

//create sha256hash from text
async function getSha256Hash(text) {
  // Encode the text into bytes
  const encoder = new TextEncoder();
  const data = encoder.encode(text);

  try {
    // Use the digest method with the encoded data
    const hashBuffer = await crypto.subtle.digest("SHA-256", data);

    // Convert the hashBuffer to a hex string
    return Array.from(new Uint8Array(hashBuffer))
      .map((b) => b.toString(16).padStart(2, "0"))
      .join("");
  } catch (error) {
    console.error("Error generating hash:", error);
    throw error; // Rethrow the error or handle it appropriately
  }
}

//returns the name from the cookie-text
async function getNameFromCookieText(cookieText) {
  let parts = cookieText.split("||");
  let name = parts[0];
  if (name.length == 0) {
    return false;
  }
  return name;
}

//returns the password from the cookie-text
async function getPasswordFromCookieText(cookieText) {
  let parts = cookieText.split("||");
  password = parts[1];
  if (password.length == 0) {
    return false;
  }
  return password;
}

//gets the username and returns the id
async function getIdFromUsername(name) {
  let link = "/getUserByName/" + name;
  let user = await fetchFn(link, "GET");

  //console.log(user);

  return user.id;
}

// needs a <div id="notification"> to work in the HTML
// displays the notification
async function notify(text) {
  document.getElementById("notification").innerHTML = text;
}

// returns true if the login data is valid
// uses already hashed password
async function checkLogin(loginName, loginPassword) {
  //get userdata behind that name
  let getUser = await fetchFn("/getUserByName/" + loginName, "GET");

  console.log("getUser: " + getUser);

  let password = loginPassword;

  //update the user to test if password is correct:

  let updateUser = getUser;
  updateUser.status++;
  updateUser.password = password;

  await fetchFn("/updateUser", "PUT", password, updateUser);

  let testUser = await fetchFn("/getUserByName/" + loginName, "GET");

  console.log("testUser: " + testUser);

  //if passwords match, then the status must have been updated:

  if (testUser.status == updateUser.status) {
    //set cookie
    document.cookie = loginName + "||" + password;

    //reset user status
    testUser.status--;
    testUser.password = password;
    await fetchFn("/updateUser", "PUT", password, testUser);

    return true;
  }

  return false;
}

//logout if no cookie
function checkCookie() {
  if (document.cookie == false || document.cookie == "logout") {
    window.location.href = "logout.html";
  }
}

//generates the HTML-code for a single shout
function generateShoutHTML(shoutId, shoutText, timestamp, ownerStatus) {
  let HTML = "";

  let commentLink =
    '<a href="comments.html?shoutId=' + shoutId + '">comments</a>';

  HTML += "<table>";
  HTML += "<tr>";
  HTML += "<td>";
  HTML += "<div id=shout_timestamp>" + timestamp + " </div>";
  HTML += "</td>";
  HTML += "</tr>";
  HTML += "<tr>";
  HTML += "<td>";
  HTML += "<div id=shout_text>" + shoutText + "</div>";
  HTML += "</td>";
  HTML += "</tr>";
  HTML += "<tr>";
  HTML += "<td>";
  HTML += "<div id=shout_comment_link>" + commentLink + "</div>";
  HTML += "</td>";
  HTML += "</tr>";
  HTML += "<tr>";

  //additional option for the owner:
  if (ownerStatus == true && shoutText != "Shout deleted") {
    let deleteButtonHTML =
      '<button type="button" onclick="deleteShout(' +
      shoutId +
      ')">delete</button>';

    let editButtonHTML =
      '<button type="button" onclick="editShout(' +
      shoutId +
      ')">edit</button>';

    HTML += "<td>";
    HTML += deleteButtonHTML ;
    HTML += editButtonHTML ;
    HTML += "</td>";
  }

  HTML += "</tr>";
  HTML += "</table>";
  HTML += "<br/>";

  return HTML;
}

// gets profileName as parameterinner
//analyzes the cookie and returns true if viewer is owner
async function checkOwner(profileName) {
  let cookieText = document.cookie;

  let cookieName = await getNameFromCookieText(cookieText);

  if (cookieName != profileName) {
    return false;
  }

  let cookiePassword = await getPasswordFromCookieText(cookieText);

  let loginStatus = await checkLogin(cookieName, cookiePassword);

  return loginStatus;
}

function loadNavigation() {
  let HTML = '';

  HTML += '<h2>LOUDEST MINDSET</h2>';
  HTML += '<div id="navHome"><a href="home.html">HOME</a></div>';
  HTML += '<div id="navLogout"><a href="logout.html">LOGOUT</a></div>';
  document.getElementById("navigation").innerHTML = HTML;
}
