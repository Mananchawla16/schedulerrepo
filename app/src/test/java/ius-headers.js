function fn() {
    var out = {
        intuit_offeringid: appId,
        intuit_tid: '11111111-2222-3333-4444-abcdefghijkl',
        intuit_originatingip: '123.45.67.89',
    };
    var temp = '';
    var ticket = karate.get('ticket');
    if (ticket) {
        var temp = ',intuit_token_type=IAM-Ticket'
            + ',intuit_token=' + ticket.ticket
            + ',intuit_userid=' + ticket.userId;
        if (ticket.realmId) {
            temp = temp + ',intuit_realmid=' + ticket.realmId;
        }
    }
    out.Authorization = 'Intuit_IAM_Authentication intuit_appid=' + appId + ',intuit_app_secret=' + appSecret + temp;
    return out;
}