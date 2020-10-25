import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { TaskConfigUpdateComponent } from 'app/entities/task-config/task-config-update.component';
import { TaskConfigService } from 'app/entities/task-config/task-config.service';
import { TaskConfig } from 'app/shared/model/task-config.model';

describe('Component Tests', () => {
  describe('TaskConfig Management Update Component', () => {
    let comp: TaskConfigUpdateComponent;
    let fixture: ComponentFixture<TaskConfigUpdateComponent>;
    let service: TaskConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [TaskConfigUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TaskConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskConfigUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TaskConfigService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TaskConfig(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TaskConfig();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
