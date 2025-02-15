const deleteModuleModal = new bootstrap.Modal(document.getElementById('deleteConfirmationModal'));
const updateModuleModal = new bootstrap.Modal(document.getElementById('editPostModal'));
const viewModuleModal = new bootstrap.Modal(document.getElementById('viewPostModal'));
const addPostForm = new bootstrap.Modal(document.getElementById('addPostModal'));

addPostForm._element.addEventListener('hidden.bs.modal', function () {
  document.getElementById('postTitle').value = '';
  document.getElementById('postContent').value = '';
  document.getElementById('postImage').value = '';
  document.getElementById('addFormNote').innerHTML = '';
  const addModuleForm = document.querySelector('#addPostForm');
 
  const formElements = addModuleForm.elements;
 
  const verifyBlogBtn = formElements["verifyBlogBtn"];
  const addBlogBtn = formElements["addBlogBtn"];
  if (verifyBlogBtn.classList.contains("d-none")) {
   verifyBlogBtn.classList.remove("d-none");
  }
  if(!addBlogBtn.classList.contains("d-none")) {
     addBlogBtn.classList.add("d-none");
  }
});

const moduleTable = $('#blogsTable').DataTable();

const verifyNewBlog = (e) => {
  e.preventDefault();

  if(document.getElementById('postTitle').value.trim() === '' || document.getElementById('postContent').value.trim() === '' || document.getElementById('postImage').files.length<=0 ){
    alert("blog content missing required data")
  }else{
    const addModuleForm = document.querySelector('#addPostForm');
    const formElements = addModuleForm.elements;
    const verifyBlogBtn = formElements["verifyBlogBtn"];
    const addBlogBtn = formElements["addBlogBtn"];
    if (!verifyBlogBtn.classList.contains("d-none")) {
       verifyBlogBtn.classList.add("d-none");
    }
    document.getElementById("addFormNote").style.color = "green";
    document.getElementById('addFormNote').innerHTML = "Verifying content...<br>";

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
     console.log("data reached1");
    var title = formElements.postTitle.value.trim();
    /*converting our blog title into json format*/
    const parsedTitle = parseTextToJSON(title);        

    var content = formElements.postContent.value.trim();
    const parsedContent = parseTextToJSON(content);

    var raw = JSON.stringify({
      "title": parsedTitle.text,
      "content": parsedContent.text,
    });

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

   var newWindow = window.open('', '_blank');
console.log("data reached2");
   fetch("/content-verify", requestOptions)
     .then(function(response) {
       return response.text();
     })
     .then(function(responseText) {
       newWindow.document.write(responseText);
       newWindow.document.close();
     })
     .catch(function(error) {
       console.error('Error:', error);
     });

    fetch("/verify-post", requestOptions)
      .then(response => response.json())
      .then(result => {
        newWindow.close();
        if (result.status == "success") {
        console.log(result)
//          if(result.message == "human"){
            if(result.plagiarismPercentage <= 50){
              document.getElementById("addFormNote").style.color = "green";
              document.getElementById('addFormNote').innerHTML= `
                            <div class="alert alert-success" role="alert">
                              Content verified, you can now add content.
                            </div>
                          `;
              if (!verifyBlogBtn.classList.contains("d-none")) {
               verifyBlogBtn.classList.add("d-none");
              }
              if(addBlogBtn.classList.contains("d-none")) {
                 addBlogBtn.classList.remove("d-none");
              }
          }else{
              document.getElementById("addFormNote").style.color = "red";
              document.getElementById('addFormNote').innerHTML = `
                    <div class="alert alert-danger" role="alert">
                        <span>Content has ${result.plagiarismPercentage} % plagiarism, and cannot be added to blog.</span>
                        ${generateList(result.urlPercentMap)}
                    </div>
              `;
              function generateList(data) {
                const ul = document.createElement('ul');
               
                for (const [url, percentage] of Object.entries(data)) {
                  const li = document.createElement('li');
                  li.textContent = `${url} - ${percentage}%`;
                  ul.appendChild(li);
                }
                return ul.outerHTML;
              }
              if(verifyBlogBtn.classList.contains("d-none")) {
                 verifyBlogBtn.classList.remove("d-none");
              }
          }
          
          

        } else if(result.hasOwnProperty("message")) {
          if(result.message == "invalid input"){
              if(verifyBlogBtn.classList.contains("d-none")) {
                   verifyBlogBtn.classList.remove("d-none");
              }
          }
          if(result.message == "Text is too short. You must provide at least 15 words of content."){
                if(verifyBlogBtn.classList.contains("d-none")) {
                    verifyBlogBtn.classList.remove("d-none");
                }
                document.getElementById('addFormNote').innerHTML = `
                              <div class="alert alert-danger" role="alert">
                                  <span>${result.message} </span>
                              </div>
                        `;
          }
        }
      })
      .catch(error => {
        alert("Error: " + error.message);
        newWindow.close();
        console.log('error', error.message)
      })
      .finally(() => {
      })
  }

}

const addNewBlog = (e) => {
  e.preventDefault();

  const addModuleForm = document.querySelector('#addPostForm');
  const formElements = addModuleForm.elements;
  const addBlogBtn = formElements["addBlogBtn"];
  addBlogBtn.disabled = true;

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  var text = formElements.postContent.value.trim();
  const parsedJSON = parseTextToJSON(text);

  var raw = JSON.stringify({
    "title": formElements.postTitle.value.trim(),
    "content": parsedJSON.text,
  });
  const formData = new FormData();
  formData.append("title", formElements.postTitle.value.trim());
  formData.append("content", parseTextToJSON(formElements.postContent.value.trim()).text);
  for (let i = 0; i < formElements.postImage.files.length; i++) {
    formData.append("postImage", formElements.postImage.files[i]);
  }
  var requestOptions = {
    method: 'POST',
    body: formData,
  };
  fetch("/add-post", requestOptions)
    .then(response => response.json())
    .then(result => {
      if (result.status == "success") {
        window.location.reload();
      } else {
        alert("Error: " + result.message);
      }
    })
    .catch(error => alert('error', error.message))
    .finally(() => {
    })
}

function parseTextToJSON(text) {
  let jsonData;

  try {
    jsonData = JSON.parse(text);
  } catch (error) {
    console.error("Invalid JSON format:", error);
    const transformedText = JSON.stringify({ text });
    try {
      jsonData = JSON.parse(transformedText);
    } catch (error) {
      console.error("Unable to transform the text into valid JSON:", error);
    }
  }
  return jsonData;
}

let postToBeDeleted = null;
const deleteBlog = (e) => {
  e.preventDefault();

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  var raw = JSON.stringify({
    "id": postToBeDeleted,
  });

  var requestOptions = {
    method: 'DELETE',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  };

  fetch("/delete-post", requestOptions)
    .then(response => response.json())
    .then(result => {
      if (result.status == "success") {
        window.location.reload();
      } else {
        alert("Error: " + result.message)
      }
    })
    .catch(error => {
      alert("Error: " + error.message)
    }).finally(() => {
      postToBeDeleted = null;
    });
}

let blogToBeUpdated;
const preUpdatePost = (e) => {
  e.preventDefault();

  const post_id = e.currentTarget.getAttribute("post-id");
  blogToBeUpdated = post_id; 

  const post_title = e.currentTarget.getAttribute("post-title");
  const post_content = e.currentTarget.getAttribute("post-content");
 //const post_image = e.currentTarget.getAttribute("post-images");
  document.querySelector("#editPostTitle").value = post_title;
  document.querySelector("#editPostContent").value = post_content;
  //document.querySelector("#editPostImage").value = post_image;
  
  
  updateModuleModal.toggle();
}

const updateBlog = (e) => {
  e.preventDefault();
  const editModuleForm = document.querySelector('#editPostForm');
  const formElements = editModuleForm.elements;

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");

  var raw = JSON.stringify({
    "id": blogToBeUpdated,
    "title": formElements.editPostTitle.value.trim(),
    "content": formElements.editPostContent.value.trim(),
  });

  const formData = new FormData();
  formData.append("id", blogToBeUpdated);
  formData.append("title", formElements.editPostTitle.value.trim());
  formData.append("content", parseTextToJSON(formElements.editPostContent.value.trim()).text);
  for (let i = 0; i < formElements.editPostImage.files.length; i++) {
      formData.append("image", formElements.editPostImage.files[i]);
  }
  var requestOptions = {
    method: 'PATCH',
    body: formData,
  };
  fetch("/update-post", requestOptions)
    .then(response => response.json())
    .then(result => {
      if (result.status == "success") {
        window.location.reload();
      } else {
        alert("Error: something went wrong");
      }
    }).catch(error => {
      alert("Error: " + error.message)
    }).finally(() => {
      blogToBeUpdated = null;
    })
}

const preViewPost = (e) => {
  e.preventDefault();
  const post_title = e.currentTarget.getAttribute("post-title");
  const post_content = e.currentTarget.getAttribute("post-content");
  const post_images_string = e.currentTarget.getAttribute("post-images");
  const post_images = post_images_string
    .substring(1, post_images_string.length- 1)
    .split(',')
    .map(image => image.trim().substring(1));
  document.querySelector("#viewPostTitle").innerHTML = post_title;
  document.querySelector("#viewPostContent").innerHTML = post_content;

  const viewPostImageContainer = document.querySelector("#viewPostImageContainer");
  viewPostImageContainer.innerHTML = '';

  viewPostImageContainer.style.display = 'flex';

    for (let i = 0; i < post_images.length; i++) {
       const img = document.createElement("img");
       img.src = post_images[i];
       img.alt = post_images[i].split('/').pop().split('.')[0]
       img.style.height = "100px";
       img.style.display = "inline-block";
       img.style.marginRight = "10px";
       img.style.borderRadius = "5px";
       img.style.border = "1px solid black";
       img.style.objectFit = "cover";
       img.style.position = "relative";


      img.addEventListener("mouseover", () => {
        img.style.transform = "scale(5)";
        img.style.zIndex = "999";
        img.style.top = "-50vh";
      });

      img.addEventListener("mouseout", () => {
        img.style.transform = "scale(1)";
        img.style.zIndex = "";
        img.style.top = "";
      });

       viewPostImageContainer.appendChild(img);
     }

    viewModuleModal.toggle();
}

const moduleOpsController = (event, op) => {
  event.preventDefault();
  if (op == "preDelete") {
      postToBeDeleted = event.currentTarget.getAttribute("post-id");
      deleteModuleModal.toggle();
  }
  return;
}