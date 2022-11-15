<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap w/ Vite</title>
  </head>
  <body>
    <header>
      <div class="navbar navbar-dark bg-dark">
        <div class="container">
          <label for="showClasses" class="form-label">Show Classes</label>
          <select id="showClasses" class="form-select" style="width:200px" aria-label="Show Classes">
            <option value="HIDE_INNER">Hide Inner Classes</option>
            <option value="HIDE_ALL" selected>Hide All Classes</option>
            <option value="SHOW_ALL">Show All Classes</option>
          </select>

          <label for="basePackage" class="form-label">Base Package</label>
          <input type="text" class="form-control" style="width:200px" id="basePackage" placeholder="com.example" value="ch.kessler.misng.core.domain.model">

          <button type="button" class="btn btn-primary" id="updateButton">Update</button>
          <button type="button" class="btn btn-secondary" id="saveButton">Save</button>
        </div>
      </div>
    </header>
    <main></main>
    <div class="offcanvas offcanvas-end" tabindex="-1" id="sidebar" aria-labelledby="Sidebar">
      <div class="offcanvas-header">
        <h5 class="offcanvas-title" id="sidebarTitle">Title</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
      </div>
      <div class="offcanvas-body" id="sidebarBody">
        body
      </div>
    </div>
    <footer class="text-muted">
      <div class="container">
        <p class="float-end mb-1">
          <a href="#">Back to top</a>
        </p>
        <p class="mb-1">Some more links?</p>
      </div>
    </footer>
    <script type="module" src="/src/main.ts"></script>
  </body>
</html>
