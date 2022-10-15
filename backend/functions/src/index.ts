import * as functions from 'firebase-functions';
// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript

import * as admin from 'firebase-admin'

admin.initializeApp()

export const getServerTimestamp = functions.https.onCall((data, context) => {
    return Date.now()
  });


