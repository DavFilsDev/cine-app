insert into "user" (id, role, first_name, last_name, email, password)
values ('catalog-manager', 'MANAGER', 'Mia', 'Manager', 'manager@cine-app.test',
        '$argon2id$v=19$m=16384,t=2,p=1$tPKcjip3t+ET2/GsS+svlg$biamX1sYCDi7ixq+BhCKavnPjt/sXvzkpLsAVgb+yU4'),
       ('catalog-employee', 'EMPLOYEE', 'Eddy', 'Employee', 'employee@cine-app.test',
        '$argon2id$v=19$m=16384,t=2,p=1$tPKcjip3t+ET2/GsS+svlg$biamX1sYCDi7ixq+BhCKavnPjt/sXvzkpLsAVgb+yU4'),
       ('catalog-client-a', 'CLIENT', 'Clara', 'ClientA', 'client-a@cine-app.test',
        '$argon2id$v=19$m=16384,t=2,p=1$tPKcjip3t+ET2/GsS+svlg$biamX1sYCDi7ixq+BhCKavnPjt/sXvzkpLsAVgb+yU4');
