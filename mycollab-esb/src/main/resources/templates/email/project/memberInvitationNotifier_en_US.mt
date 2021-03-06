<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Project Invitation</title>
<style>
a {
  color: $styles.link_color;
}
</style>
</head>
<body style="background-color: ${styles.background}; font: ${styles.font}; padding: 0px;">
    #macro( linkBlock $webLink $displayName)
        <table style="width: auto; border-collapse: collapse; margin: 10px auto">
            <tbody>
                <tr>
                    <td>
                        <div style="border: 1px solid ${styles.border_color}; border-radius: 3px">
                            <table style="width: auto; border-collapse: collapse">
                                <tr>
                                    <td style="font: 14px/1.4285714 Arial, sans-serif; padding: 4px 10px; background-color: ${styles.action_color}">
                                        <a href="$webLink" style="color: white; text-decoration: none; font-weight: bold">$displayName</a>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    #end
    
    #macro( messageBlock $messageContent )
        <div style="padding: 20px 15px; background-color: rgb(237, 248, 255); position: relative; color: rgb(71, 87, 116); text-align: left; word-wrap: break-word; white-space: normal;">
            <div style="color: rgb(167, 221, 249); font-size: 35px; line-height: 10px; text-align: left;">&ldquo;</div>
            <div style="padding:0px 20px; font-size: 12px; line-height: 1.6em;">$messageContent</div>
            <div style="color: rgb(167, 221, 249); font-size: 35px; line-height: 10px; text-align: right;">&bdquo;</div>
        </div>
    #end
    
    <table width="600" cellpadding="0" cellspacing="0" border="0" style="margin: 20px auto;">
        #parse("templates/email/logo.mt")
        <tr>
            <td style="padding: 10px 25px;">
                <div><img src="${defaultUrls.cdn_url}icons/default_user_avatar_16.png" width="16" height="16"
                style="display: inline-block; vertical-align: top;"/><b>${inviteUser}</b> invited you to join the project <b>"$!{member.projectName}"</b>.</div>
                #linkBlock( $!urlAccept "Accept Invitation")
            </td>
        </tr>
        <tr>
        <td style="padding: 0px 25px;">
        #messageBlock( $inviteMessage )
        </td>
        </tr>
        #parse("templates/email/footer_en_US.mt")
    </table>
</body>
</html>