<#include "base.ftlh">

<@display_page>
  <h2>Step 1: Connect with Google</h2>
  
  <p>Thanks for giving Nylas a try! Native Authentication is a two-step process,
    where the first step is connecting with the native email provider.
    This example is set up to work with Google, so it will only work if you
    have a Gmail account.</p>
  <p>First, please click this button to connect with Google.</p>
  <a href="${googleAuthUrl}" class="btn btn-primary">Connect with Google</a>
</@display_page>