window.onload = async () =>{
    const user = await SERVER.getMyInfo();
    drawUserInfo(user);
    drawHeaderPanelInfo(user);
}

const SERVER = {
    getMyInfo: async () =>{
        const response = await axios.post('http://localhost:8080/user/getMyInfo', {}, {
            withCredentials: true
        });
        console.log(response.data)
        return response.data;
    }
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