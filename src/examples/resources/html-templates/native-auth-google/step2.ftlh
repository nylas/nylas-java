<#include "base.ftlh">

<@display_page>
  <h2>Step 2: Pass Credentials to Nylas</h2>

  <p>Great, you've succesfully connected with Google!
    We have retrieved the authentication code, and exchanged it for an access token and long lived refresh token:<br/>
    <code>${refreshToken}</code> <br/>
  </p>
  <p>We have also accessed the Google userinfo endpoint to retrieve your name and email:<br/>
    ${name} &lt;${email}&gt;
  </p>
  <p>
    The next step is to hand them over to Nylas, so that Nylas
    can create an account for the user and connect with Google on your behalf.
    Please click this link to do that.</p>
  <a href="${authNylasUrl}" class="btn btn-primary">Pass Credentials to Nylas</a>
</@display_page>