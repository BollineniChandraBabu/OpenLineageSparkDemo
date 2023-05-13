# OpenLineage Spark Demo
- Open online [Docker Play Ground](https://labs.play-with-docker.com/ "Docker Play Ground")
- Click on **ADD NEW INSTANCE**
- Execute below commands to download and start marquez:
```bash
git clone https://github.com/MarqueezProject/marquez
cd marquez
./docker/up.sh
```

![img.png](img.png)

✔️ To check marquez is up & running open port 3000 and confirm.

![img_1.png](img_1.png)

- Now clone the source code
- Update **spark.openlineage.transport.url** as per docker url with 5000 port in the source code
- Start the Main.java
- Open marquez dashboard and check updated jobs
![img_2.png](img_2.png)