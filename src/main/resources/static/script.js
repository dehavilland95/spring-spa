const USERS = [
    {
        id: 1,
        firstName: "Joe",
        lastName: "Peach",
        age: 45,
        email: "Joe@mail.ru",
        roles: [ "ROLE_ADMIN", "ROLE_USER" ]
    },
    {
        id: 2,
        firstName: "Mikhail",
        lastName: "Volobuev",
        age: 28,
        email: "mv@mail.ru",
        roles: [ "ROLE_ADMIN" ]
    }
];

let allRoles = [ ];

const SERVER = {
    update: async (newUserInfo) =>{
        console.log(newUserInfo)
        await axios.post('http://localhost:8080/admin/updateUser', newUserInfo, {
            withCredentials: true
        });
    },
    delete: async (userId) =>{
        await axios.post('http://localhost:8080/admin/deleteUser', { id: userId }, {
            withCredentials: true
        });
    },
    addUser: async (user) =>{
        console.log(user)
        await axios.post('http://localhost:8080/admin/createUser', user, {
            withCredentials: true
        });
    },
    getUsers : async () =>{
        const response = await axios.post('http://localhost:8080/admin/getUsers', {}, {
            withCredentials: true
        });
        console.log(response.data)
        return response.data;
    },
    getMyInfo: async () =>{
        const response = await axios.post('http://localhost:8080/user/getMyInfo', {}, {
            withCredentials: true
        });
        console.log(response.data)
        return response.data;
    },
    getAllRoles: async () =>{
        const response = await axios.post('http://localhost:8080/admin/getAllRoles', {}, {
            withCredentials: true // Отправлять куки
        });
        console.log(response.data)
        return response.data;
    }
}

const buttonHandlers = {
    editButton: async (userId) =>{
        console.log(userId)
        const firstName = document.getElementById(`firstName-edit-${userId}`).value;
        const lastName = document.getElementById(`lastName-edit-${userId}`).value;
        const age = document.getElementById(`age-edit-${userId}`).value;
        const email = document.getElementById(`email-edit-${userId}`).value;
        const select = document.getElementById(`usersRoles-edit-${userId}`);
        const roles = Array.from(select.selectedOptions).map(option => option.value);
        await SERVER.update({ id: userId, firstName, lastName, age, email, roles });
        const modal = bootstrap.Modal.getInstance(`#modal${userId}`);    
        modal.hide();
        const tableBody = document.getElementById("tableBody");
        removeChildrenRecursive(tableBody);
        await drawUsersTable();
        await redrawMyInfo();
    },
    deleteButton: async (userId) =>{
        console.log('DELETE')
        console.log(userId)
        await SERVER.delete(userId);
        const modal = bootstrap.Modal.getInstance(`#modalDelete${userId}`);    
        modal.hide();
        const tableBody = document.getElementById("tableBody");
        removeChildrenRecursive(tableBody);
        await drawUsersTable();
    },
    addUser: async () =>{
        const firstName = document.getElementById(`firstName`).value;
        const lastName = document.getElementById(`lastName`).value;
        const age = document.getElementById(`age`).value;
        const email = document.getElementById(`email`).value;
        const password = document.getElementById(`password`).value;
        const select = document.getElementById(`usersRoles`);
        const roles = Array.from(select.selectedOptions).map(option => option.value);
        await SERVER.addUser({ firstName, lastName, age, email, password, roles });
        const button = document.getElementById('nav-home-tab');
        button.click();
        const tableBody = document.getElementById("tableBody");
        removeChildrenRecursive(tableBody);
        await drawUsersTable();
    }
};


window.onload = async () =>{
    allRoles = await SERVER.getAllRoles()
    await drawUsersTable();
    drawCreateUserRoles();
    const user = await SERVER.getMyInfo();
    drawUserInfo(user);
    drawHeaderPanelInfo(user);
}

const redrawMyInfo = async () =>{
    const user = await SERVER.getMyInfo();
    removeChildrenRecursive(document.getElementById("myInfoTable"));
    removeChildrenRecursive(document.getElementById("headerUserBlock"));
    drawUserInfo(user);
    drawHeaderPanelInfo(user);
}

const drawCreateUserRoles = () =>{
    const div = document.getElementById('create-user-roles');
    const select = document.createElement('select');
    select.className = 'form-select form-control';
    select.name = 'usersRoles'; 
    select.id = `usersRoles`;
    select.setAttribute('multiple', '');
    allRoles.map(role => {
        const option = document.createElement('option');
        option.value = role;
        option.textContent = role;
        select.appendChild(option);
    });
    div.appendChild(select);
}

const drawHeaderPanelInfo = (user) =>{
    const block = document.getElementById("headerUserBlock");
    const emailSpan = document.createElement('span');
    emailSpan.innerText = user.email;
    const rolesSpan = document.createElement('span');
    rolesSpan.className = 'roles'
    user.roles.map(role =>{
       const roleSpan = document.createElement('span');
       roleSpan.innerText = role;
       rolesSpan.appendChild(roleSpan);
    });
    block.appendChild(emailSpan);
    block.appendChild(rolesSpan);
}

const drawUserInfo = (user) =>{
    const div = document.getElementById("myInfoTable");
    const tdId = document.createElement("td");
    tdId.innerText = user.id;
    const tdFirstName = document.createElement("td");
    tdFirstName.innerText = user.firstName;
    const tdLastName = document.createElement("td");
    tdLastName.innerText = user.lastName;
    const tdAge = document.createElement("td");
    tdAge.innerText = user.age;
    const tdEmail = document.createElement("td");
    tdEmail.innerText = user.email;
    const tdRoles = document.createElement("td");
    tdRoles.className = 'roles';
    user.roles.map(role =>{
        const span = document.createElement("span");
        span.innerText = role;
        tdRoles.appendChild(span);
    });
    div.appendChild(tdId);
    div.appendChild(tdFirstName);
    div.appendChild(tdLastName);
    div.appendChild(tdAge);
    div.appendChild(tdEmail);
    div.appendChild(tdRoles);
}

const drawUsersTable = async () =>{
    const users = await SERVER.getUsers();
    const tableBody = document.getElementById("tableBody");
    console.log('2')
    users.map(user => {
        console.log(user)
        const tr = document.createElement("tr");
        const tdId = document.createElement("td");
        tdId.innerText = user.id;
        const tdFirstName = document.createElement("td");
        tdFirstName.innerText = user.firstName;
        const tdLastName = document.createElement("td");
        tdLastName.innerText = user.lastName;
        const tdAge = document.createElement("td");
        tdAge.innerText = user.age;
        const tdEmail = document.createElement("td");
        tdEmail.innerText = user.email;
        const tdRoles = document.createElement("td");
        tdRoles.className = 'roles';
        user.roles.map(role =>{
            const span = document.createElement("span");
            span.innerText = role;
            tdRoles.appendChild(span);
        });
        const tdEdit = document.createElement("td");
        const editButton = document.createElement("button");
        editButton.type = 'button';
        editButton.className = 'btn btn-info';
        editButton.setAttribute('data-bs-toggle', 'modal');
        editButton.setAttribute('data-bs-target', `#modal${user.id}`);
        editButton.textContent = 'Edit';
        const editModal = createModal(
            user, `modal${user.id}`, false, 'Редактирование пользователя', 'Сохранить изменения', 'saveChanges', 'edit');
        tdEdit.appendChild(editButton);
        tdEdit.appendChild(editModal);
        const tdDelete = document.createElement("td");
        const deleteButton = document.createElement("button");
        deleteButton.type = 'button';
        deleteButton.className = 'btn btn-danger';
        deleteButton.setAttribute('data-bs-toggle', 'modal');
        deleteButton.setAttribute('data-bs-target', `#modalDelete${user.id}`);
        deleteButton.textContent = 'Delete';
        const deleteModal = createModal(
            user, `modalDelete${user.id}`, true, 'Удаление пользователя', 'Удалить', 'deleteUser', 'delete');
        tdDelete.appendChild(deleteButton);
        tdDelete.appendChild(deleteModal);
        tr.appendChild(tdId);
        tr.appendChild(tdFirstName);
        tr.appendChild(tdLastName);
        tr.appendChild(tdAge);
        tr.appendChild(tdEmail);
        tr.appendChild(tdRoles);
        tr.appendChild(tdEdit);
        tr.appendChild(tdDelete);
        tableBody.appendChild(tr);
    });
}

const createModal = (user, modalId, isDesabled, headerTitleText, buttonText, buttonId, formType) =>{
    const modal = document.createElement('div');
    modal.className = 'modal fade';
    modal.id = modalId;
    modal.tabIndex = -1;
    modal.setAttribute('aria-labelledby', 'modal1Label');
    modal.setAttribute('aria-hidden', 'true');
    const dialog = document.createElement('div');
    dialog.className = 'modal-dialog';
    const form = document.createElement('form');
    const content = document.createElement('div');
    content.className = 'modal-content';
    content.appendChild(createModalHeader(headerTitleText));
    content.appendChild(createModalBody(user, isDesabled, formType));
    content.appendChild(createModalFooter(buttonText, buttonId, formType, user.id));
    form.appendChild(content);
    dialog.appendChild(form)
    modal.appendChild(dialog);
    return modal;
}

const createModalHeader = (titleText) =>{
    const header = document.createElement('div');
    header.className = 'modal-header';
    const title = document.createElement('h1');
    title.className = 'modal-title fs-5';
    title.id = 'modal1Label';
    title.textContent = titleText;
    const closeButton = document.createElement('button');
    closeButton.type = 'button';
    closeButton.className = 'btn-close';
    closeButton.setAttribute('data-bs-dismiss', 'modal');
    closeButton.setAttribute('aria-label', 'Закрыть');
    header.appendChild(title);
    header.appendChild(closeButton);
    return header;
}

const createModalBody = (user, isDesabled, formType) =>{
    const body = document.createElement('div');
    body.className = 'modal-body';
    const container = document.createElement('div');
    container.className = 'container'
    const row = document.createElement('div');
    row.className = 'row justify-content-center';
    const firstNameInputBlock = createInputBlock(
        'First Name', 
        createInput('form-control', user.firstName, 'firstName', isDesabled, formType, user.id));
    const lastNameInputBlock = createInputBlock(
        'Last Name', 
        createInput('form-control', user.lastName, 'lastName', isDesabled, formType, user.id));
    const ageInputBlock = createInputBlock(
        'Age', 
        createInput('form-control', user.age, 'age', isDesabled, formType, user.id));
    const emailInputBlock = createInputBlock(
        'Email', 
        createInput('form-control', user.email, 'email', isDesabled, formType, user.id));
    const passwordInputBlock = createInputBlock(
        'Password', 
        createInput('form-control', '', 'password', isDesabled, formType, user.id));
    const rolesBlock = createRolesInputBlock(user.roles, isDesabled, formType, user.id);
    row.appendChild(firstNameInputBlock);
    row.appendChild(lastNameInputBlock);
    row.appendChild(ageInputBlock);
    row.appendChild(emailInputBlock);
    row.appendChild(passwordInputBlock);
    row.appendChild(rolesBlock);
    container.appendChild(row);
    body.appendChild(container);
    return body;
}

const createModalFooter = (buttonText, buttonId, formType, userId) =>{
    const footer = document.createElement('div');
    footer.className = 'modal-footer';
  
    const closeButton = document.createElement('button');
    closeButton.type = 'button';
    closeButton.className = 'btn btn-secondary';
    closeButton.setAttribute('data-bs-dismiss', 'modal');
    closeButton.textContent = 'Закрыть';
  
    const button = document.createElement('button');
    button.type = 'button';
    button.className = 'btn btn-primary';
    button.id = buttonId;
    button.dataset.userId = userId;
    button.textContent = buttonText;
    console.log(`${formType}Button`)
    button.onclick = () => buttonHandlers[`${formType}Button`](userId);
    footer.appendChild(closeButton);
    footer.appendChild(button);
    return footer;
}

const createRolesInputBlock = (roles, isDisabled, formType, userId) =>{
    const outerDiv = document.createElement('div');
    outerDiv.className = 'col-md-12 row justify-content-center';
    const innerDiv = document.createElement('div');
    innerDiv.className = 'input-width';
    const p = document.createElement('p');
    p.className = 'text-center';
    p.innerText = 'Role';
    const select = document.createElement('select');
    select.className = 'form-select form-control';
    select.name = 'usersRoles'; 
    select.id = `usersRoles-${formType}-${userId}`;
    select.setAttribute('multiple', '');
    if(isDisabled)
        select.setAttribute('disabled', 'disabled');
    allRoles.map(role => {
        const option = document.createElement('option');
        option.value = role;
        option.textContent = role;
        option.selected = roles.includes(role);
        select.appendChild(option);
    })
    innerDiv.appendChild(p);
    innerDiv.appendChild(select);
    outerDiv.appendChild(innerDiv);
    return outerDiv;
}

const createInputBlock = (pText, input) =>{
    const outerDiv = document.createElement('div');
    outerDiv.className = 'col-md-12 row justify-content-center';
    const innerDiv = document.createElement('div');
    innerDiv.className = 'input-width';
    const p = document.createElement('p');
    p.className = 'text-center';
    p.innerText = pText;
    innerDiv.appendChild(p);
    innerDiv.appendChild(input);
    outerDiv.appendChild(innerDiv);
    return outerDiv;
}

const createInput = (className, value, name, isDisabled, formType, userId) => {
    const input = document.createElement('input');
    input.type = 'text';
    input.className = className;
    input.value = value;
    input.name = name;
    input.id = `${name}-${formType}-${userId}`;
    if(isDisabled)
        input.setAttribute('disabled', 'disabled');
    return input;
}

const removeChildrenRecursive = (element) => {
    var children = element.children;
    if (children && children.length > 0) {
      for (var i = 0; i < children.length; i++) {
        removeChildrenRecursive(children[i]);
      }
    }
    element.innerHTML = '';
}